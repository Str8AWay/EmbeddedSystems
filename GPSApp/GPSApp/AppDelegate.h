//
//  AppDelegate.h
//  GPSApp
//
//  Created by Jason Laqua on 11/7/13.
//  Copyright (c) 2013 Embedded Systems. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import "GCDAsyncUdpSocket.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate, CLLocationManagerDelegate, GCDAsyncUdpSocketDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) CLLocationManager *locationManager;
@property (strong, nonatomic) CLLocation *location;
@property (strong, nonatomic) CLHeading *heading;
@property (strong, nonatomic) GCDAsyncUdpSocket *socket;
@property (strong, nonatomic) CLLocation *destination;
@property BOOL sendPackets;

@end
