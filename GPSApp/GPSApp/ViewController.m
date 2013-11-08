//
//  ViewController.m
//  GPSApp
//
//  Created by Jason Laqua on 11/7/13.
//  Copyright (c) 2013 Embedded Systems. All rights reserved.
//

#import "ViewController.h"
#import "AppDelegate.h"
#import <CoreLocation/CoreLocation.h>

#define GPS_FORMAT @"(%f, %f)"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(handleHeadingChange:)
                                                 name:@"headingChanged"
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(handleLocationChange:)
                                                 name:@"locationChanged"
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(handleCorrectionChange:)
                                                 name:@"correctionChanged"
                                               object:nil];
    
    AppDelegate *delegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
    self.headingLabel.text = [NSString stringWithFormat:@"%f", delegate.heading.trueHeading];
    self.gpsLabel.text = [NSString stringWithFormat:GPS_FORMAT, delegate.location.coordinate.latitude, delegate.location.coordinate.longitude];
    self.correctionLabel.text = [NSString stringWithFormat:@"%f",delegate.correction];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)handleHeadingChange:(id)object
{
    AppDelegate *delegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
    self.headingLabel.text = [NSString stringWithFormat:@"%f", delegate.heading.trueHeading];
}

- (void)handleLocationChange:(id)object
{
    AppDelegate *delegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
    self.gpsLabel.text = [NSString stringWithFormat:GPS_FORMAT, delegate.location.coordinate.latitude, delegate.location.coordinate.longitude];
}

- (void)handleCorrectionChange:(id)object
{
    AppDelegate *delegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
    self.correctionLabel.text = [NSString stringWithFormat:@"%f", delegate.correction];
}

@end
