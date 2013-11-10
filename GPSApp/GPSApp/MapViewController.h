//
//  MapViewController.h
//  GPSApp
//
//  Created by Jason Laqua on 11/9/13.
//  Copyright (c) 2013 Embedded Systems. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>

@interface MapViewController : UIViewController

@property (weak, nonatomic) IBOutlet MKMapView *mapView;

@end
