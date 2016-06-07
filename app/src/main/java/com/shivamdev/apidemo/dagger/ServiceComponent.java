package com.shivamdev.apidemo.dagger;

import com.shivamdev.apidemo.network.NetworkModule;
import com.shivamdev.apidemo.network.TwitterApi;

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
