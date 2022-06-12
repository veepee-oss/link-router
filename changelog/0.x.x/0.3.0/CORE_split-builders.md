---
title: Create individual builders for each supported routing type
url: https://git.vptech.eu/veepee/offerdiscovery/products/front-mobile/android/link-router/-/merge_requests/14
author: Julio Cesar Bueno Cotta
---
- Change we make it easy to create a router that only supports only type of routing (Activity, Fragment, DeepLink and Composables). 
- Split the `RouterBuilder` internals in the delegate builders. 


Hopefully this will help we split the artefact in smaller ones in the future.
