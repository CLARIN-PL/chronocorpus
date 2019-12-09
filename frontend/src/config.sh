#!/bin/bash
echo "window.appConfig = { API_URL: '${API_URL}'} " >> config.js
cat config.js
apachectl -D FOREGROUND
