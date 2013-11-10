//
//  MapViewController.m
//  GPSApp
//
//  Created by Jason Laqua on 11/9/13.
//  Copyright (c) 2013 Embedded Systems. All rights reserved.
//

#import "MapViewController.h"
#import "AppDelegate.h"

@interface MapViewController ()

@end

@implementation MapViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(addDestination)
                                                 name:@"destinationChanged"
                                               object:nil];
    [self addDestination];
}

- (void)viewWillAppear:(BOOL)animated
{
    CLLocationCoordinate2D zoomLocation;
    zoomLocation.latitude = 43.037448;
    zoomLocation.longitude= -87.929534;
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(zoomLocation, 500, 500);
    
    [self.mapView setRegion:viewRegion animated:YES];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)addDestination
{
    AppDelegate *delegate = (AppDelegate *)[UIApplication sharedApplication].delegate;

    // Remove annotations
    [self.mapView removeAnnotations:self.mapView.annotations];
    
    // Add an annotation
    MKPointAnnotation *point = [[MKPointAnnotation alloc] init];
    point.coordinate = delegate.destination.coordinate;
    point.title = @"Destination";
    [self.mapView addAnnotation:point];
}

@end
