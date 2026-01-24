package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import evolutionmod.powers.LustPower;
import evolutionmod.powers.ShadowsPower;

public class PlayingRoughAction extends AbstractGameAction {

	private DamageInfo info;

	public PlayingRoughAction(AbstractCreature source, AbstractCreature target, DamageInfo info, AttackEffect effect) {
		this.source = source;
		this.target = target;
		this.info = info;
		this.setValues(target, info);
		this.actionType = ActionType.DAMAGE;
		this.attackEffect = effect;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
	}

	public void update() {
		this.tickDuration();
		if (this.isDone) {
			this.target.damage(this.info);
			if (this.target.lastDamageTaken/2 > 0) {
				if (!target.isDying) {
					AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.target, this.source,
									new LustPower(this.target, this.target.lastDamageTaken/2)));
				}
			}
		}
	}
}
