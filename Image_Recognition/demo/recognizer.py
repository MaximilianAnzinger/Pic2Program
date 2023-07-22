'''
Created on 22 Oct 2018

@author: Maximilian Anzinger
'''

import sys
import tensorflow as tf


class Recognizer(object):
    
    '''
    '''
    
    def __init__(self, image_dir):
        self.dev_mode = True
        self.image_dir = image_dir
        self.classifications_file_dir = "/TF_data/classifications.txt"
        self.graph_dir = "/TF_data/graph.pb"

    def main(self):
        
        if self.dev_mode:
            print(" --- dev_mode: enabled --- ")
            
        image_name = sys.argv[1]
        
        if self.dev_mode:
            print("dev_out: image_name: " + image_name)
            
        self.image_recognition(self.image_dir + image_name)
        
    def image_recognition(self, image_path):
        
        image_data = tf.gfile.FastGFile(image_path, 'rb').read()
        
        if self.dev_mode:
            print("dev_out: classifications_file_dir: " + self.image_dir)
            
        classifications = [classification.rstrip() for classification in tf.gfile.GFile(self.classifications_file_dir)]
        
        if self.dev_mode:
            classifications_str = ""
            for classification in classification:
                classifications_str += classification
            print("dev_out: classifications: " + classifications_str)
        
        if self.dev_mode:
            print("dev_out: graph_dir: " + self.graph_dir)
            
        with tf.gfile.FastGFile(self.graph_dir, 'rb') as graph_file:
            graph_def = tf.GraphDef()
            graph_def.ParseFromString(graph_file.read())
            _ = tf.import_graph_def(graph_def, name='')
        
        with tf.Session() as session:
            softmax_tensor = session.graph.get_tensor_by_name('final_result:0')
        
            predictions = session.run(softmax_tensor, {'DecodeJpg/contents:0': image_data})
            
            ranking = predictions[0].argsort()[-len(predictions[0]):][::-1]
            
            for node_id in ranking:
                output_str = classifications[node_id]
                result_value = predictions[0][node_id]
                print('%s (value = %.5f)' % (output_str, result_value))
                
