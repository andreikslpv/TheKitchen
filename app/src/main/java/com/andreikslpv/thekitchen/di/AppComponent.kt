package com.andreikslpv.thekitchen.di

 import android.content.Context
 import com.andreikslpv.thekitchen.presentation.ui.MainActivity
 import com.andreikslpv.thekitchen.presentation.ui.fragments.AuthFragment
 import com.andreikslpv.thekitchen.presentation.ui.fragments.HomeFragment
 import com.andreikslpv.thekitchen.presentation.ui.fragments.RecipeFragment
 import com.andreikslpv.thekitchen.presentation.ui.recyclers.IngredientViewHolder
 import com.andreikslpv.thekitchen.presentation.ui.recyclers.RecipeNewViewHolder
 import com.andreikslpv.thekitchen.presentation.ui.recyclers.RecipeCatalogViewHolder
 import com.andreikslpv.thekitchen.presentation.vm.*
 import dagger.BindsInstance
 import dagger.Component
 import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DomainModule::class, DataModule::class, FirebaseModule::class, DatabaseModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    //методы для того, чтобы появилась возможность внедрять зависимости в требуемые классы
    fun inject(authFragment: AuthFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(recipeFragment: RecipeFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(authViewModel: AuthViewModel)
    fun inject(mainViewModel: MainViewModel)
    fun inject(profileViewModel: ProfileViewModel)
    fun inject(homeViewModel: HomeViewModel)
    fun inject(catalogViewModel: CatalogViewModel)
    fun inject(favoritesViewModel: FavoritesViewModel)
    fun inject(filtersViewModel: FiltersViewModel)
    fun inject(recipeViewModel: RecipeViewModel)
    fun inject(recipeCatalogViewHolder: RecipeCatalogViewHolder)
    fun inject(recipeNewViewHolder: RecipeNewViewHolder)
    fun inject(ingredientViewHolder: IngredientViewHolder)
}