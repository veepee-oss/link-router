# link-router

This library contains the basic infrastructure for routing deepLinks, activities and fragments within a multi-module application
in a way that a feature module does not need to explicitly depend of another.

### Usage
The basic concept is the same for all routing, at the app startup we register all deeplink, activities and fragments mappers.
And in a separated module (like :routes) we share basic implementations that works as indirections to the real implementations.

### Activities
To make it possible to route your Activity into other modules we need to implement `ActivityName`,
`ActivityLink<ActivityName>` and `ActivityNameMapper<ActivityName>` that reflects you needs.

- `ActivityName` acts as key when routing to your Activity  #shared
- `ActivityLink` binds the `ActivityName` with parameters we want to pass to that Activity and #shared
- `ActivityNameMapper<ActivityName>` maps the `ActivityName` into an Activity class. #internal

#### Things we need to share
The `ActivityLink` and `ActivityName` are used to route, so the client module needs to have access to that implementation.
You can place it in a `:routes` module or in any other shared  module that suits your needs (`:mydomain:routes`, maybe?).

````kotlin
object MyActivityName : ActivityName
````

````kotlin
class MyActivityLink(
    override val parameter: MyActivityParameter
) : ActivityLink<MyActivityName> {

    override val activityName: MyActivityName = MyActivityName
}

@Parcelize
data class MyActivityParameter(val data: String) : ParcelableParameter

````
#### Things that we keep inside
With that key we can implement an `ActivityNameMapper<ActivityName>` in your feature module.

```kotlin

object MyActivityNameMapper : ActivityNameMapper<MyActivityName> {
    override val supportedNames: Array<MyActivityName> = arrayOf(MyActivityName)

    override fun map(activityLink: ActivityLink<MyActivityName>): Class<out Activity> {
        return MyActivityName::class.java
    }
}

```

#### Pro tip
If your module has multiple activities you can use an enum for your `ActivityName`
 and only one implementation of `ActivityNameMapper<ActivityName>`.
````kotlin
enum class  MyFeatureModuleActivitiesNames : ActivityName {
    MyActivityName,
    MyOtherActivityName
}
````

and

```kotlin

object MyFeatureModuleActivityNameMapper :
    ActivityNameMapper<MyFeatureModuleActivitiesNames> {
    override val supportedNames: Array<MyFeatureModuleActivitiesNames> = MyFeatureModuleActivitiesNames.values()

    override fun map(activityLink: ActivityLink<MyFeatureModuleActivitiesNames>): Class<out Activity> {
        return when (activityLink.activityName) {
            MyFeatureModuleActivitiesNames.MyActivityName -> MyActivity::class.java
            MyFeatureModuleActivitiesNames.MyOtherActivityName -> MyOtherActivity::class.java
        }
    }
}

```

### Fragments
Activities and Fragments have similar abstractions, so if you know how to route Activities, you know how to route Fragments.
| Activity             | Fragment             |
|:--------------------:|:--------------------:|
| ActivityNameMapper   | FragmentNameMapper   |
| ActivityName         | FragmentName         |

### DeepLinks
To each DeepLink we need an implementation of `DeepLinkMapper<DeepLink>`, 
where we describe for which apps that DeepLink is supported, what is the DeepLink authority and 
what stack of Activities should be created.
````kotlin
object MyDeepLinkMapper : DeepLinkMapper<UriDeepLink> {
    override val supportedSchemes: Array<Scheme> = arrayOf(Schemes.publicAppSchemes)
    override val supportedAuthority: String = "my schemne"

    override fun stack(deepLink: UriDeepLink): Array<ActivityLink<ActivityName>> {
        return arrayOf(
            ExternalDeepLinkRouterActivityLink(deepLink.parameter)
        )
    }

    override fun canHandle(deepLink: DeepLink): Boolean {
        return super.canHandle(deepLink) && deepLink is UriDeepLink
    }
}

````

### Runtime registration
You can use the `Application.create()` method or Googles StartUp library to register your mappers.
Use `RouterBuilder` to register `ActivityNameMapper`s, `FragmentNameMapper`s and `DeepLinkMapper`s.

````kotlin
class MyFeatureModuleInitializer : AppStartUp<Unit>() {
    override fun create(context: Context) {
        with(GlobalRouterBuilder) {
            add(MyActivityNameMapper)
            add(MyFragmentNameMapper)
            add(MyDeepLinkMapper)
        }
    }
}
````
