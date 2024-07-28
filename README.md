# BTW Fishing, Animal, Dung and Seed Tweaks

Based on BTW-Community/BTW-gradle-fabric-example

https://github.com/BTW-Community/BTW-gradle-fabric-example

For use with Minecraft Addon Better Than Wolves CE v2.1.4

Modifies fishing speed, chances for hemp seeds when using hoe on grass (small chance of wheat seeds also), dung mechanics for other animals and food that initiates breeding for cows, sheep, chickens, pigs and wolves.

## Changes in detail:

### Fishing

    Fishing base bite chance (before modifiers) changed from 1/1500 to 1/400 chance per tick

### Seeds

    Chance for hemp seeds when using hoe on grass blocks changed from 1/25 to 1/17
    Added 1/90 chance for wheat seeds when using hoe on grass

### Animals/Breeding

    Chickens can now also produce eggs when fed Hemp Seeds
    Cows can now breed when fed Wheat
    Sheep can now breed when fed Wheat
    Wolves can now breed when fed raw or cooked Fish
    Pigs can now breed when fed raw or cooked Fish

### Dung

    Cows, sheep and pigs now produce dung using the same mechanic as wolves at differing rates
    Animals make dung a lot quicker, changed from 20 minutes on average for wolves to 4 minutes
    Other animals make dung between 4-6 minutes depending on the animal
    Dung velocity reduced
