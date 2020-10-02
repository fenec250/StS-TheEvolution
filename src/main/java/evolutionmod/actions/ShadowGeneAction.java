package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class ShadowGeneAction extends AbstractHalfTargetedAction {

	private int damage;
	private int weak;

	public ShadowGeneAction(AbstractPlayer player, AbstractMonster monster, int damage, int weak) {
		super(player, monster);
		this.source = player;
		this.target = monster;
		this.damage = damage;
		this.weak = weak;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DEBUFF;
	}

	public void update() {
		if (!updateTarget()) {
			this.isDone = true;
			return;
		}
		if (damage > 0) {
			AbstractDungeon.actionManager.addToTop(new DamageAction(
					this.target, new DamageInfo(this.source, this.damage, DamageInfo.DamageType.THORNS),
					AttackEffect.NONE, true));
		}
		AbstractDungeon.actionManager.addToTop(
				new ApplyPowerAction(this.target, this.source, new WeakPower(this.target, this.weak, false), this.weak));

		this.isDone = true;

		this.tickDuration();
	}
}
