package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.orbs.SuccubusGene;

public class SeduceAction extends AbstractGameAction {

	private DamageInfo info;
	private boolean healSelf;

	public SeduceAction(AbstractCreature source, AbstractCreature target, boolean healSelf, DamageInfo info, AttackEffect effect) {
		this.source = source;
		this.target = target;
		this.info = info;
		this.setValues(target, info);
		this.actionType = ActionType.DAMAGE;
		this.attackEffect = effect;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.healSelf = healSelf;
	}

	public void update() {
		this.tickDuration();
		if (this.isDone) {
			this.target.damage(this.info);
			if (this.target.lastDamageTaken > 0) {
				if (healSelf) {
					AbstractDungeon.actionManager.addToTop(new HealAction(this.source, this.source, this.target.lastDamageTaken));
				} else {
					AbstractDungeon.actionManager.addToTop(new GainBlockAction(this.source, this.source, this.target.lastDamageTaken));
				}
				if (!target.isDying) {
					AbstractDungeon.actionManager.addToTop(new GainBlockAction(this.target, this.source, this.target.lastDamageTaken));
				}
			}
		}
	}
}
