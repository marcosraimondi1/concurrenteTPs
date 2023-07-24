import re

LOG_PATH = ".\\..\\data\\log.txt"

test_str = ""

# open file
if (test_str == ""):
    print("[INFO]  \tOPENING FILE \t\t", LOG_PATH)
    with open(LOG_PATH, 'r') as file:
        # read first line
        test_str = file.readline()

# regex = r"T0(?P<T0>(?:T(?:\d+))*?)(?:(?:T1(?P<T1>(?:T(?:\d+))*?)T3)|(?:T2(?P<T2>(?:T(?:\d+))*?)T4))(?P<T3T4>(?:T(?:\d+))*?)(?:(?:T5(?P<T5>(?:T(?:\d+))*?)T7)|(?:T6(?P<T6>(?:T(?:\d+))*?)T8))(?P<T7T8>(?:T(?:\d+))*?)(?:(?:T10(?P<T10>(?:T(?:\d+))*?)T12)|(?:T9(?P<T9>(?:T(?:\d+))*?)T11))(?P<T11T12>(?:T(?:\d+))*?)T13(?P<T13>(?:T(?:\d+))*?)T14(?P<T14>(?:T(?:\d+))*?)"

regex = r"T0(?P<T0>(?:T(?:\d+))*?)(?:(?:T1(?P<T1>(?:T(?!1T)(?:\d+))*?)T3)|(?:T2(?P<T2>(?:T(?!2T)(?:\d+))*?)T4))(?P<T3T4>(?:T(?:\d+))*?)(?:(?:T5(?P<T5>(?:T(?!5T)(?:\d+))*?)T7)|(?:T6(?P<T6>(?:T(?!6T)(?:\d+))*?)T8))(?P<T7T8>(?:T(?:\d+))*?)(?:(?:T10(?P<T10>(?:T(?!(?:9T|10T|11T))(?:\d+))*?)T12)|(?:T9(?P<T9>(?:T(?!(?:9T|10T|12T))(?:\d+))*?)T11))(?P<T11T12>(?:T(?:\d+))*?)T13(?P<T13>(?:T(?!13T)(?:\d+))*?)T14(?P<T14>(?:T(?:\d+))*?)"
subst = "\\g<T0>\\g<T1>\\g<T2>\\g<T3T4>\\g<T5>\\g<T6>\\g<T7T8>\\g<T9>\\g<T10>\\g<T11T12>\\g<T13>\\g<T14>"

result = re.subn(regex, subst, test_str)

while result[1] > 0:
    result = re.subn(regex, subst, result[0])

if len(result[0]) > 0:
    print("[ERROR] \tVALIDATION FAILED")
else:
    print("[INFO]  \tVALIDATION PASSED")

print("[INFO]  \tVALIDATION RESULT \t", result)
