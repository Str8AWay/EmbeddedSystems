//
//  CLLocation+Bearing.h
//  GPSApp
//
//  Created by Jason Laqua on 11/7/13.
//  Copyright (c) 2013 Embedded Systems. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>


@interface CLLocation (Bearing)

-(double) bearingToLocation:(CLLocation *) destinationLocation;

@end
