package com.fantasyclash.zeus.di

import com.example.clock_o_mentiapatient.Repositories.RepoImplemenation.AuthRepoImplementation
import com.example.clock_o_mentiapatient.Repositories.RepoImplemenation.BookDoctorImplementation
import com.example.clock_o_mentiapatient.Repositories.RepoImplemenation.ClockTestRepoImplementation
import com.example.clock_o_mentiapatient.Repositories.RepoInterfaces.AuthRepoInterface
import com.example.clock_o_mentiapatient.Repositories.RepoInterfaces.BookDoctorRepoInterface
import com.example.clock_o_mentiapatient.Repositories.RepoInterfaces.ClockTestRepoInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun authRepo(authRepoImplementation: AuthRepoImplementation): AuthRepoInterface

    @Binds
    abstract fun clockRepo(clockTestRepoImplementation: ClockTestRepoImplementation) : ClockTestRepoInterface

    @Binds
    abstract fun bookDoctorRepo(bookDoctorImplementation: BookDoctorImplementation) : BookDoctorRepoInterface
}