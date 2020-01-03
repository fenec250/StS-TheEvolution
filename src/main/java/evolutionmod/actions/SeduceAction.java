package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class SeduceAction extends AbstractGameAction {

	private DamageInfo info;

	public SeduceAction(AbstractCreature source, AbstractCreature target, DamageInfo info, AttackEffect effect) {
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
			if (this.target.lastDamageTaken > 0) {
				AbstractDungeon.actionManager.addToTop(new GainBlockAction(this.source, this.source, this.target.lastDamageTaken));
				if (!target.isDying) {
					AbstractDungeon.actionManager.addToTop(new GainBlockAction(this.target, this.source, this.target.lastDamageTaken));
				}
			}
//			if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
//				AbstractDungeon.actionManager.clearPostCombatActions();
//			}
		}
	}
}
