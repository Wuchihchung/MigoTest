# MigoTest
11/18 Migo online test


// Wuchihchung.twgmail.com
// Author : Bryan Wu
// Date : 2021/11/18

// TO-DO
// - Use Volley as a lightweight http client, need to review any memory leakage case while official release. Volley has leakage issue sometimes.
// - Use a callback function to retrieve network status to avoid using POLLING each time sending an API to BE



// Better design for question1
//  It's clear that I can understand the use case is using public API(production server) for end user and using private API(staging server) in R&D development envirenment.
//  But put the logic in app (client) side is not a good idea. If there's any logic change, we need to publish apk again and it also takes time for all end users to upgrade their apk.
// 
//  The good idea is :
//    1. Build two apks each build, one is production build and another one is test build. Production build is for official release, test build is for R&D internal test.
//       Use build flag to control the API url when build time, use public url in production apk and use private url in test apk.
//      2. Control the APU url via adb command. By default the URL is public url, and you can use a adb command to change url to private url. Relaunch apk to make it affect.
      
Question 1 :
  1. Get network status at the beginning when app is launching
  2. Show up network status to user by a toast
  3. Please click the floating button at bottom-right corner to get API status
  4. According to the requirement, Wifi has the 1st priority, which means once wifi is connected then use private API


Question 2 :
  Not finished.
