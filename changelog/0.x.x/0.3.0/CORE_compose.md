---
title: Add support for Compose
url: https://git.vptech.eu/veepee/offerdiscovery/products/front-mobile/android/link-router/-/merge_requests/13
author: Julio Cesar Bueno Cotta
---
- Update compileSdkVersion to 32, as it was required by the latest compose version
- Add `ComposableName`, `ComposableLink`, `ComposableParameter`, `ComposableNameMapper` and `ComposableLinkRouter` to handle composables routing in a similar way that we do for Activities, Fragements and DeepLinks.
We offer `LinkRouterContainer` and `ComposableFor` composables as a way to have a more "compose like" API to access the required composable.

``` kotlin
@Composable
private fun MyRootComposition() {
    LinkRouterContainer(router = router) {
        ComposableFor(FeatureBComposableLink("Some text"))
    }
}

```

Sadly, we can't have composable functions references so we have to change our strategy for matching a composable with its ComposableLink at the `ComposableNameMapper` implementaitons.

``` kotlin
...
    @Composable
    override fun Map(
        composableLink: ComposableLink<ComposableBNames>,
        modifier: Modifier
    ) {
        when (composableLink) {
            is FeatureBComposableLink -> BasicText(composableLink.parameter.message, modifier)
        }
    }
...

```

Instead of matching with the `ComposableName` it is way more useful to match with the instance of the `ComposableLink` so we have access to the `ComposableParameter` in a typed way.

## NOTES:
- I also think that it is odd to have composable methods name capitalised even when they are part of a class, but the link is dumb and I think this is better over having the lint complaining everywhere about this.
