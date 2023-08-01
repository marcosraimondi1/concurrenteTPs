import re


def python_to_java(regex, subst):
    new_regex = re.subn(r"\?P<", "?<", regex)

    new_subst = re.subn(r"\\g<(.*?)>", "${\\g<1>}", subst)

    print("\nJAVA REGEX:")
    print(new_regex[0])
    print("\nJAVA SUBST:")
    print(new_subst[0])


def java_to_python(regex, subst):
    new_regex = re.subn(r"\?<", "?P<", regex)

    new_subst = re.subn(r"{", "<", subst)
    new_subst = re.subn(r"}", ">", new_subst[0])

    print("\nPYTHON REGEX:")
    print(new_regex[0])
    print("\nPYTHON SUBST:")
    print(new_subst[0])


regex = input("regex: ")
subst = input("subst: ")

print("Python to Java (1)")
print("Java to Python (2)")
option = input("Select: ")

if option == "1":
    python_to_java(regex, subst)
else:
    java_to_python(regex, subst)
