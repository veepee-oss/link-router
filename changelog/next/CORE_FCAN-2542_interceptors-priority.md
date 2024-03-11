---
title: requires a priority for adding interceptors
url: https://jira.vptech.eu/browse/FCAN-2542
author: Julio Cesar Bueno Cotta
---
To enable distributed addition of interceptors (where the order of execution is important) I am adding a `priority` argument to link-router's `add` methods for interceptors.


Closes FCAN-2542
