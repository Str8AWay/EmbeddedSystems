//
//  AppDelegate.m
//  GPSApp
//
//  Created by Jason Laqua on 11/7/13.
//  Copyright (c) 2013 Embedded Systems. All rights reserved.
//

#import "AppDelegate.h"
#import "CLLocation+Bearing.h"
#import "UIView+Toast.h"

#define INCOMING_PORT 8000
#define COORDINATE_REGEX @"^(-?\\d+\\.\\d+),(-?\\d+\\.\\d+)$"
#define BEARING_FORMAT @"BEARING:%d"

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    [self startStandardUpdates];
    [self.locationManager startUpdatingLocation];
    [self.locationManager startUpdatingHeading];
    
    
    NSError *bindError;
    NSError *recError;
    self.socket = [[GCDAsyncUdpSocket alloc] initWithDelegate:self delegateQueue:dispatch_get_main_queue()];
    [self.socket bindToPort:INCOMING_PORT error:&bindError];
    [self.socket beginReceiving:&recError];
    
    return YES;
}
							
- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    [self.locationManager stopUpdatingLocation];
    [self.locationManager stopUpdatingHeading];
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
    [self.locationManager startUpdatingLocation];
    [self.locationManager startUpdatingHeading];
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

- (void)startStandardUpdates
{
    // Create the location manager if this object does not
    // already have one.
    if (nil == self.locationManager)
        self.locationManager = [[CLLocationManager alloc] init];
    
    self.locationManager.delegate = self;
    self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    
    // Set a movement threshold for new events.
    self.locationManager.distanceFilter = kCLDistanceFilterNone; // meters
}

- (void)locationManager:(CLLocationManager *)manager didUpdateHeading:(CLHeading *)newHeading
{
    NSDate *eventDate = newHeading.timestamp;
    NSTimeInterval howRecent = [eventDate timeIntervalSinceNow];
    if (abs(howRecent) < 15.0) {
        self.heading = newHeading;
        if (self.sendPackets) {
            [self sendingCorrectionPacket];
        }
        [[NSNotificationCenter defaultCenter] postNotificationName:@"headingChanged" object:nil];
    }
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    CLLocation *newLocation = [locations lastObject];
    NSDate *eventDate = newLocation.timestamp;
    NSTimeInterval howRecent = [eventDate timeIntervalSinceNow];
    if (abs(howRecent) < 15.0) {
        self.location = newLocation;
        if (self.sendPackets) {
            [self sendingCorrectionPacket];
        }
        [[NSNotificationCenter defaultCenter] postNotificationName:@"locationChanged" object:nil];
    }
}

- (void)udpSocket:(GCDAsyncUdpSocket *)sock didReceiveData:(NSData *)data fromAddress:(NSData *)address withFilterContext:(id)filterContext
{
    [self.socket sendData:data toHost:VEX_IP port:VEX_PORT withTimeout:-1 tag:1];
    NSString *message = [[NSString alloc] initWithData:data
                                              encoding:NSUTF8StringEncoding];
    [self.window.rootViewController.view makeToast:message duration:0.5 position:@"bottom"];
    
    if ([message hasPrefix:@"43"]) {
        NSError *error;
        NSTextCheckingResult *match = [[NSRegularExpression regularExpressionWithPattern:COORDINATE_REGEX options:NSRegularExpressionAnchorsMatchLines error:&error] firstMatchInString:message options:NSMatchingReportCompletion range:NSMakeRange(0, message.length)];
        if (match != nil) {
            NSString *lat = [message substringWithRange:[match rangeAtIndex:1]];
            NSString *lon = [message substringWithRange:[match rangeAtIndex:2]];
            CLLocation *destination = [[CLLocation alloc] initWithLatitude:lat.doubleValue longitude:lon.doubleValue];
            self.destination = destination;
            self.sendPackets = YES;
            [[NSNotificationCenter defaultCenter] postNotificationName:@"destinationChanged" object:nil];
        }
    }
}

- (void)sendingCorrectionPacket
{
    if ([self.location distanceFromLocation:self.destination] < 1) {
        self.sendPackets = NO;
        [self.socket sendData:[@"STOP" dataUsingEncoding:NSUTF8StringEncoding] toHost:VEX_IP port:VEX_PORT withTimeout:-1 tag:1];
    } else {
        double bearingTo = [self.location bearingToLocation:self.destination];
        double convertedHeading = self.heading.trueHeading;
        convertedHeading = convertedHeading > 180 ? -(360 - convertedHeading) : convertedHeading;
        self.correction = (bearingTo - convertedHeading) * -1;
        self.correction = self.correction > 180 ? -(360 - self.correction) : self.correction;
        self.correction = self.correction < -180 ? (360 + self.correction) : self.correction;
        NSData *data = [[NSString stringWithFormat:BEARING_FORMAT, (int)self.correction] dataUsingEncoding:NSUTF8StringEncoding];
        [self.socket sendData:data toHost:VEX_IP port:VEX_PORT withTimeout:-1 tag:1];
        [[NSNotificationCenter defaultCenter] postNotificationName:@"correctionChanged" object:nil];
    }
}

@end
