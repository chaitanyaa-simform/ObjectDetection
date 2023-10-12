//
//  CustomObjectDetectionModule.m
//  ObjDetection
//
//  Created by Chaitanyaa Adaki on 09/10/23.
//

#import <Foundation/Foundation.h>
#import "React/RCTBridgeModule.h"

@interface RCT_EXTERN_MODULE(CustomObjectDetectionModule, NSObject)
RCT_EXTERN_METHOD(startCustomObjectDetection: (NSString *)image resolver: (RCTPromiseResolveBlock)resolve rejecter: (RCTPromiseRejectBlock)reject)

@end
