//
//  ObjectDetectionModule.m
//  ObjDetection
//
//  Created by Chaitanyaa Adaki on 05/10/23.
//

#import <Foundation/Foundation.h>
#import "React/RCTBridgeModule.h"

@interface RCT_EXTERN_MODULE(ObjectDetectionModule, NSObject)
RCT_EXTERN_METHOD(startObjectDetection: (NSString *) image resolver: (RCTPromiseResolveBlock)resolve rejecter: (RCTPromiseRejectBlock)reject)

@end
