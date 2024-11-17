# Generate a file with random data , size is 4mb
import random

def generate_data():
    with open("data.txt", "w") as f:
        for i in range(1024 * 1024):
            f.write(str(random.randint(1, 1000)) + "\n")

if __name__ == "__main__":
    generate_data()