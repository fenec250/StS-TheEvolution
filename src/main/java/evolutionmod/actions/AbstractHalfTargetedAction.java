package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.powers.MarkPower;

public abstract class AbstractHalfTargetedAction extends AbstractGameAction {

	public AbstractHalfTargetedAction(AbstractPlayer player, AbstractMonster monster) {
		this.source = player;
		this.target = monster;
	}

	public boolean updateTarget() {
		if ((AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
			return false;
		}
		if (this.target == null || this.target.isDeadOrEscaped()) {
			this.target = AbstractDungeon.getMonsters().monsters.stream()
					.filter(m -> m.hasPower(MarkPower.POWER_ID) && !m.isDeadOrEscaped())
					.findAny()
					.orElse(AbstractDungeon.getRandomMonster());
			if (this.target == null) {
				return false;
			}
		}
		return true;
	}
}
