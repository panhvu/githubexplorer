package app.com.githubexplorer.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.com.githubexplorer.detail.DetailViewModel
import app.com.githubexplorer.main.SearchViewModel
import app.com.githubexplorer.utils.BaseViewModelFactory
import app.com.githubexplorer.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @Binds
    internal abstract fun bindFactory(modelFactory: BaseViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun bindDetailViewModel(detailViewModel: DetailViewModel): ViewModel
}