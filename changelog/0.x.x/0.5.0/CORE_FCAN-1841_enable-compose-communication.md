---
title: Enable composables communication
url: https://jira.vptech.eu/browse/FCAN-1841
author: Julio Cesar Bueno Cotta
---


Android components have ways of implicit communication, for instance
- Activities -> Activities -> OnActivityResult/Contracts
- Fragments -> Fragments -> Fragment Result API

The started activity don't need to know which Activity called it, the Fragments sending results are not required to know who is listening for it's results.


But how about Composables -> ???


Compose does not provide an out of the box solution to send implicit events to the callers.


### What did I do?

This MR tries to enable that communication in a **somewhat** type safe way.

Our approach avoids "lambda forwarding" by using a `LinkRouterEventHandler` that is instantiated when accessing a `LinkRouterEventHandlerContainer`.

Example :
``` kotlin
sealed interface MyData : ComposableEvent {
    data class MyData1(val foo: String) : MyData
    data class MyData2(val foo: String) : MyData
}

@Composable
fun MyScreen() {
    LinkRouterEventHandlerContainer<MyData>(onEvent = {
        println(it)
    },
        content = {
            MyDataComposable()
        }
    )
}

@Composable
private fun MyDataComposable() {
    MySecondLayerDataComposable()
}

@Composable
private fun MySecondLayerDataComposable() {
    val handler = LocalLinkRouterEventHandler.current
    BasicText(
        "hello world",
        modifier = Modifier.clickable { handler.publish(MyData.MyData1("bar")) })
}
```

Event though `MySecondLayerDataComposable` is a leaf composable and does not contain a "onClick : () -> Unit" lambda, the onClick event arrives to `onEvent` lambda in `MyScreen` composable.

We say that this API is **somewhat** type safe because there is no guarantees that a `LinkRouterEventHandlerContainer` will be listening for the published type.

For instance:
``` kotlin
@Composable
private fun MySecondLayerDataComposable() {
    val handler = LocalLinkRouterEventHandler.current
    BasicText(
        "hello world",
        modifier = Modifier.clickable { handler.publish("foobar") })
}
```
Would cause an error at **runtime** as there is no registered `LinkRouterEventHandlerContainer` that can handle Strings.


The new composable `ComposableFor` is meant to be called with `ComposableLinkWithEvent` implementations and offers a lambda with the expected event type that can be emitted

``` kotlin
ComposableFor(
                link = FeatureBComposableLink("1 - AActivity sent this text to a Composable in B Module"),
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.Yellow)
            ) { event ->
                text = event.messageSize.toString()
            }
```

The `FeatureBComposableLink` class is defined as 
```
class TestComposableBLink(
    override val parameter: TestComposableBParameter
) : ComposableLinkWithEvent<TestComposableName, TestComposableBLinkEvent> {
    override val composableName: TestComposableName = TestComposableName.TestComposableB
}

data class TestComposableBParameter(val message: String) : ComposableParameter

data class TestComposableBLinkEvent(val foo: String) : ComposableEvent
```

Notice that it implements `ComposableLinkWithEvent`, not `ComposableLink` as we need the `ComposableEvent` type to ensure the correct match of types when routing this link.


### Notes:
- Is this fine? With this we open the gate to a new class of bugs in the project, runtime errors due to missing event handlers.
- Should `ComposableEvent` be renamed to `ComposableResult` ? I don't quite like the `Event` suffix.

### QA:
1) what will happen if I call `handler.publish()` with a type that is not directly handled by the nearest EventHandler ? 
The event will be forwarded to the next EventHandler until it reaches an EventHandler that can handle it or the topmost EventHandler where we are raising en exception and crashing.

2) Can I use this to avoid the lambdas inside my feature composables?
Yes, but be aware that Google does not recommend this! We are using this in LinkRouter because we couldn't find a better way of sharing events in a generic way without rewriting the library.
As I stated above, there is no compile time check if an EventHandler is deployed in the compose hierarchy that can handle the type you want to publish. 

3) Can I use primitive types as Events?
Yes, but you **really** should think in using a sealed class or sealed interface for your public API as the caller can use a `when` to evaluate all possible states.
