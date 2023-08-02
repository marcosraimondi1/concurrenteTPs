import re

LOG_PATH = ".\\..\\data\\log.txt"

test_str = ""

# open file
if (test_str == ""):
    print("[INFO]  \tOPENING FILE \t\t", LOG_PATH)
    with open(LOG_PATH, 'r') as file:
        # read first line
        test_str = file.readline()

regex = r"(?P<I0>T0)(?P<T0>(?:T\d+)*?)(?:(?P<I1>T1)(?P<T1>(?:T\d+)*?)(?P<I3>T3)|(?P<I2>T2)(?P<T2>(?:T\d+)*?)(?P<I4>T4))(?P<T3T4>(?:T\d+)*?)(?:(?P<I5>T5)(?P<T5>(?:T\d+)*?)(?P<I7>T7)|(?P<I6>T6)(?P<T6>(?:T\d+)*?)(?P<I8>T8))(?P<T7T8>(?:T\d+)*?)(?:(?P<I10>T10)(?P<T10>(?:T\d+)*?)(?P<I12>T12)|(?P<I9>T9)(?P<T9>(?:T\d+)*?)(?P<I11>T11))(?P<T11T12>(?:T\d+)*?)(?P<I13>T13)(?P<T13>(?:T\d+)*?)(?P<I14>T14)(?P<T14>(?:T\d+)*?)"
subst = "-\\g<I0>\\g<I1>\\g<I3>\\g<I2>\\g<I4>\\g<I5>\\g<I7>\\g<I6>\\g<I8>\\g<I10>\\g<I12>\\g<I9>\\g<I11>\\g<I13>\\g<I14>-\\g<T0>\\g<T1>\\g<T2>\\g<T3T4>\\g<T5>\\g<T6>\\g<T7T8>\\g<T9>\\g<T10>\\g<T11T12>\\g<T13>\\g<T14>"


cuenta_invariantes = {
    "T0T1T3T5T7T10T12T13T14"    : 0 ,
    "T0T1T3T5T7T9T11T13T14"     : 0 ,
    "T0T1T3T6T8T10T12T13T14"    : 0 ,
    "T0T1T3T6T8T9T11T13T14"     : 0 ,
    "T0T2T4T5T7T10T12T13T14"    : 0 ,
    "T0T2T4T5T7T9T11T13T14"     : 0 ,
    "T0T2T4T6T8T10T12T13T14"    : 0 ,
    "T0T2T4T6T8T9T11T13T14"     : 0
}


def procesar_resultado(result):
    resultList = result[0].split("-")

    resto = ""
    for inv in resultList:
        if inv == "":
            continue
        
        if inv in cuenta_invariantes.keys():
            cuenta_invariantes[inv] += 1
            continue

        resto += inv

    return resto


result = re.subn(regex, subst, test_str)


while result[1] > 0:
    resto = procesar_resultado(result)
    result = re.subn(regex, subst, resto)

if len(result[0]) > 0:
    print("[ERROR] \tVALIDATION FAILED")
else:
    print("[INFO]  \tVALIDATION PASSED")

print("[INFO]  \tVALIDATION RESULT \t", result)
print("[INFO]  \tINVARIANT COUNT \t",cuenta_invariantes)