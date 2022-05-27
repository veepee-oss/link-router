---
title: Add support to DialogFragments routing
url: https://git.vptech.eu/veepee/offerdiscovery/products/front-mobile/android/link-router/-/merge_requests/10
author: Julio Cesar Bueno Cotta
---
This MR allows explicit routing for DialogFragments as the usage of this kind of fragment is kinda different from regular fragments, i.e. we `show()` dialog fragments instead of committing them.

- Added DialogFragmentName, DialogFragmentLink to enable the registration of the desired fragment type and `dialogFragmentFor(dialogFragmentLink): DialogFragment` for routing.
- Updated sample with an example
