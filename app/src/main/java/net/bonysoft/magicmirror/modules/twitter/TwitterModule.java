package net.bonysoft.magicmirror.modules.twitter;

import com.novoda.notils.logger.simple.Log;

import net.bonysoft.magicmirror.BuildConfig;
import net.bonysoft.magicmirror.modules.DashboardModule;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterModule implements DashboardModule {

    private static final String QUERY = "#droidconde";

    private final TwitterStream twitterStream;
    private final TwitterListener listener;
    private boolean running = false;

    public static TwitterModule newInstance(TwitterListener listener) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        Configuration configuration = configurationBuilder
                .setOAuthConsumerKey(BuildConfig.TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(BuildConfig.TWITTER_CONSUMER_SECRET)
                .setOAuthAccessToken(BuildConfig.TWITTER_ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(BuildConfig.TWITTER_ACCESS_TOKEN_SECRET)
                .build();
        TwitterStream twitterStream = new TwitterStreamFactory(configuration).getInstance();
        return new TwitterModule(twitterStream, listener);
    }

    TwitterModule(TwitterStream twitterStream, TwitterListener listener) {
        this.twitterStream = twitterStream;
        this.listener = listener;

        initStream();
    }

    private void initStream() {
        twitterStream.addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
                listener.onNextTweet(status);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                // No-op
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                // No-op
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                // No-op
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                Log.w(warning);
            }

            @Override
            public void onException(Exception ex) {
                Log.e(ex);
            }
        });
    }

    @Override
    public void update() {
        if (running) {
            return;
        }
        running = true;

        twitterStream.filter(QUERY);
    }

    @Override
    public void stop() {
        twitterStream.cleanUp();
        running = false;
    }

    public interface TwitterListener {

        void onNextTweet(Status tweet);

    }
}
