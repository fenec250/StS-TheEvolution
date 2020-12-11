package evolutionmod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface DroneUpgrader {
    int priority();
    int upgradesLeft();
    AbstractCard getUpgradedDrone();
    void consumeUpgrades(int upgradesConsumed);
}
