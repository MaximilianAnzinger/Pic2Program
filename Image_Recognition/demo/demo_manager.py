'''
Created on 23 Oct 2018

@author: max
'''

from enum import Enum

class DemoType(Enum):
    SINGLE_CLASS = 0
    MULTI_CLASS = 1
    FAILED = -1

class DemoManager(object):
    '''
    classdocs
    '''


    def __init__(self, demoType):
        '''
        Constructor
        '''
        if demoType == DemoType.SINGLE_CLASS:
            self.runSingleClassDemo()
        elif demoType == DemoType.MULTI_CLASS:
            self.runMultiClassDemo()
        else:
            print("DemoManager: FAILED: demoType")
            exit()
            
    def runSingleClassDemo(self):
        pass
    
    def runMultiClassDemo(self):
        pass