/*
package com.shivamdev.twitterapidemo.dagger;

import com.shivamdev.twitterapidemo.network.NetworkModule;
import com.shivamdev.twitterapidemo.network.TwitterApi;

import javax.annotation.Generated;
import javax.inject.Provider;

import dagger.internal.ScopedProvider;

*/
/**
 * Created by shivamchopra on 04/06/16.
 *//*

@Generated("dagger.internal.codegen.ComponentProcessor")
public class DaggerServiceComponent implements ServiceComponent {
    private Provider<TwitterApi> twitterApiProvider;

    private DaggerServiceComponent(Builder builder) {
        assert builder != null;
        initialize(builder);
    }

    private void initialize(final Builder builder) {
        this.twitterApiProvider = ScopedProvider.create(NetworkModule_ProvideTwitterApiFactory.create(builder.networkModule));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ServiceComponent create() {
        return builder().build();
    }

    public static final class Builder {
        private NetworkModule networkModule;

        private Builder() {
        }

        public ServiceComponent build() {
            if (networkModule == null) {
                this.networkModule = new NetworkModule();
            }
            return new DaggerServiceComponent(this);
        }

        public Builder networkModule(NetworkModule networkModule) {
            if (networkModule == null) {
                throw new NullPointerException("networkModule");
            }
            this.networkModule = networkModule;
            return this;
        }
    }
}
*/
