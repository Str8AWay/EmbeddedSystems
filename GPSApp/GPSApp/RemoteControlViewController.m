//
//  RemoteControlViewController.m
//  GPSApp
//
//  Created by Jason Laqua on 11/8/13.
//  Copyright (c) 2013 Embedded Systems. All rights reserved.
//

#import "RemoteControlViewController.h"
#import "AppDelegate.h"

@interface RemoteControlViewController ()

@end

@implementation RemoteControlViewController

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
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)stopPressed:(id)sender {
    AppDelegate *delegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    if (delegate.sendPackets) {
        delegate.sendPackets = NO;
    }
    [delegate.socket sendData:[@"STOP" dataUsingEncoding:NSUTF8StringEncoding] toHost:VEX_IP port:VEX_PORT withTimeout:-1 tag:1];
}

- (IBAction)arrowPressed:(id)sender {
    AppDelegate *delegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    NSString *message = [sender accessibilityLabel];
    [delegate.socket sendData:[message dataUsingEncoding:NSUTF8StringEncoding] toHost:VEX_IP port:VEX_PORT withTimeout:-1 tag:1];
}


@end
