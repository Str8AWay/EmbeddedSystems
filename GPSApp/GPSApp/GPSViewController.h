//
//  ViewController.h
//  GPSApp
//
//  Created by Jason Laqua on 11/7/13.
//  Copyright (c) 2013 Embedded Systems. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GPSViewController : UIViewController
@property (weak, nonatomic) IBOutlet UILabel *gpsLabel;
@property (weak, nonatomic) IBOutlet UILabel *headingLabel;
@property (weak, nonatomic) IBOutlet UILabel *correctionLabel;
@property (weak, nonatomic) IBOutlet UIImageView *arrayImageView;

@end
