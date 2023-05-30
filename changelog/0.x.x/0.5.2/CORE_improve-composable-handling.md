---
title: ComposableFor methods now do a better job restricting implementations
url: https://jira.vptech.eu/browse/FCAN-1853
author: Julio Cesar Bueno Cotta
---
ComposableFor methods now do a better job restricting implementations that require a ComposableEvent to be used with the version that does not expose a lambda.

A ComposableLink now needs to specify a `ComposableEvent` in it's declaration, if it does not emit any value, we should use `NoComposableEvent` as part of the declaration, this will make the signature match the `ComposableFor` implementation without the event lambda.
