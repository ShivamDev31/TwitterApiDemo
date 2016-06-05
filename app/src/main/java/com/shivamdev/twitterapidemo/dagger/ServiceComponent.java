package com.shivamdev.twitterapidemo.dagger;

import com.shivamdev.twitterapidemo.network.NetworkModule;
import com.shivamdev.twitterapidemo.network.TwitterApi;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by shivamchopra on 04/06/16.
 */

@Singleton
@Component(modules = {NetworkModule.class})
public interface ServiceComponent {

    TwitterApi getTwitterApi();
}
