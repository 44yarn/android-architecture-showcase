package io.github.yarn44.showcase.activity

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.yarn44.showcase.core.foundation.activity.ActivityLauncher

@Module
@InstallIn(SingletonComponent::class)
abstract class ActivityLauncherModule {

    @Binds
    abstract fun bindActivityLauncher(impl: ActivityLauncherImpl): ActivityLauncher
}
