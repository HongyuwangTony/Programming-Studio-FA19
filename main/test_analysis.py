from unittest import TestCase
from analysis import *


class TestAnalysis(TestCase):
    def test_analyze_hub(self):
        self.assertTupleEqual(('Bruce Willis', 411), analyze_hub()[0])

    def test_analyze_age_group(self):
        self.assertTupleEqual((61, 818506715), analyze_age_group()[0])
