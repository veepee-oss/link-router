# link-router

This library contains the basic infrastructure for routing DeepLinks, Activities, Fragments and Composables within a multi-module application
in a way that a feature module does not need to explicitly depend of another.

### Usage
The basic concept is the same for all routing, at the app startup we register all Deeplink, Activities, Fragments and Compose mappers 
and in a separated module (like :routes) we share basic "links" that work as indirections to the real implementations.

### Abstractions
Activities, Fragments and Composables have similar abstractions, so if you know how to route Activities, you know how to route Fragments and Composables.
| Activity             | Fragment             | Composable             |
|:--------------------:|:--------------------:|:--------------------:|
| ActivityName         | FragmentName         | ComposableName         |
| ActivityLink         | FragmentLink         | ComposableLink         |
| ActivityNameMapper   | FragmentNameMapper   | ComposableNameMapper   |
| ParcelableParameter  | ParcelableParameter  | ComposableParameter    |
| ActivityLinkRouter   | FragmentLinkRouter   | ComposableLinkRouter   |


### Activities
To make it possible to route your Activity into other modules we need to implement `ActivityName`,
`ActivityLink<ActivityName>` and `ActivityNameMapper<ActivityName>` that reflects your needs.

- `ActivityName` acts as key when routing to your Activity  # lives in a shared module
- `ActivityLink` binds the `ActivityName` with parameters we want to pass to that Activity and # lives in a shared module
- `ActivityNameMapper<ActivityName>` maps the `ActivityName` into an Activity class. # on your feature module

#### Things we need to place in a shared module
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
#### Things that we keep inside our feature modules
With that `ActivityName` we can implement an `ActivityNameMapper<ActivityName>` into your feature module.

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

### DeepLinks
The handling of DeepLinks is a bit different from the other components that we support.
We don't define a `DeepLinkName` as DeepLink usually come in a String format and parsing/matching rules that are application specific.
To each DeepLink we only need an implementation of `DeepLinkMapper<DeepLink>`, 
where we describe for which schemes that DeepLink is supported, what is the DeepLink authority and 
what stack of Activities should be created.

```kotlin
object MyDeepLinkMapper : DeepLinkMapper<UriDeepLink> {
    override val supportedSchemes: Array<Scheme> = arrayOf(Schemes.publicAppSchemes)
    override val supportedAuthority: String = "my_authority"

    override fun stack(deepLink: UriDeepLink): Array<ActivityLink<ActivityName>> {
        return arrayOf(
            ExternalDeepLinkRouterActivityLink(deepLink.parameter)
        )
    }

    override fun canHandle(deepLink: DeepLink): Boolean {
        return super.canHandle(deepLink) && deepLink is UriDeepLink
    }
}

```
Since you are in charge of defining what parameters to pass to each `ActivityLink`, 
you can use it to define your app state and navigate internally to a given 
Fragment or Composable back stack that you see convenient to your business logic.


### Compose
Define your `ComposeName`, `ComposableLink`, `ComposableLinkMapper` and set in the root of your 
Composable tree a `LinkRouterContainer` with an instance of `LinkRouter` reference configured by you.
With that you can call `ComposableFor` with a `ComposableLink` and optionally a `Modifier`.

```kotlin
LinkRouterContainer(router = router) {
        ComposableFor(
            FeatureBComposableLink("Some text"),
            Modifier
        )
    }
```

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
