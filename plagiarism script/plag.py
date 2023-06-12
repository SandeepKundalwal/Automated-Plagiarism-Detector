# Run steps : 
# 1. install CopyDetector : pip3 install copydetect
# 2. Run command : python3 plag.py {Assignment file location} {Report generation location}

import os
import sys
from copydetect import CopyDetector

def test_compare_saving(tmpdir):
        config = {
          "test_directories" : [sys.argv[1]],
          "reference_directories" : [sys.argv[1]],
          "extensions" : ["py"],
          "noise_threshold" : 25,
          "guarantee_threshold" : 25,
          "display_threshold" : 0,
          "disable_autoopen" : True,
          "out_file" : tmpdir
        }
        detector = CopyDetector(config, silent=True)
        detector.run()
        detector.generate_html_report()

        # check for expected files
        # assert Path(tmpdir + "/report.html").exists()

if __name__ == "__main__":
  tmpdir = sys.argv[2]
  test_compare_saving(tmpdir)