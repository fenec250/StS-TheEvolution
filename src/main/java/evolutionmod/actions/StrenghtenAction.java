package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class StrenghtenAction extends AbstractGameAction {

	public StrenghtenAction(AbstractCreature user) {
		this.source = this.target = user;
		this.duration = this.startDuration = 0f;
		this.actionType = ActionType.SPECIAL;
	}

	public void update() {
		AbstractPower vigor = this.source.getPower(VigorPower.POWER_ID);
		if (vigor != null) {
			addToTop(new ApplyPowerAction(this.source, this.target,
					new VigorPower(this.target, vigor.amount)));
		}

		this.isDone = true;

		this.tickDuration();
	}
}
