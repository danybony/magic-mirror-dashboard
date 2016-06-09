package net.bonysoft.magicmirror.modules.twitter;

import com.novoda.notils.logger.simple.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.bonysoft.magicmirror.BuildConfig;
import net.bonysoft.magicmirror.modules.DashboardModule;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterModule implements DashboardModule {

    private static final String QUERY = "#droidconde";

    private final Twitter twitter;
    private final TwitterListener listener;

    public static TwitterModule newInstance(TwitterListener listener) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        Configuration configuration = configurationBuilder
                .setOAuthConsumerKey(BuildConfig.TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(BuildConfig.TWITTER_CONSUMER_SECRET)
                .setOAuthAccessToken(BuildConfig.TWITTER_ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(BuildConfig.TWITTER_ACCESS_TOKEN_SECRET)
                .build();
        Twitter twitter = new TwitterFactory(configuration).getInstance();
        return new TwitterModule(twitter, listener);
    }

    TwitterModule(Twitter twitter, TwitterListener listener) {
        this.twitter = twitter;
        this.listener = listener;
    }

    @Override
    public void update() {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Query query = new Query(QUERY);
                    QueryResult searchResult = twitter.search(query);

                    // TODO: iterate and show all of them with delay
                    listener.onNextTweet(searchResult.getTweets().get(0));
                } catch (TwitterException e) {
                    Log.e(e, "Error searching for new tweets");
                }
            }
        });
    }

    public interface TwitterListener {

        void onNextTweet(Status tweet);

    }
}
