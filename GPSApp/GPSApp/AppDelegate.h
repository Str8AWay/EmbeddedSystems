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

#define VEX_IP @"192.168.2.5"
#define VEX_PORT 8888

@interface AppDelegate : UIResponder <UIApplicationDelegate, CLLocationManagerDelegate, GCDAsyncUdpSocketDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) CLLocationManager *locationManager;
@property (strong, nonatomic) CLLocation *location;
@property (strong, nonatomic) CLHeading *heading;
@property (strong, nonatomic) GCDAsyncUdpSocket *socket;
@property (strong, nonatomic) CLLocation *destination;
@property double correction;
@property BOOL sendPackets;

@end
