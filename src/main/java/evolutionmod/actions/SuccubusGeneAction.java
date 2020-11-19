package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import evolutionmod.powers.LustPower;

public class SuccubusGeneAction extends AbstractHalfTargetedAction {

	private int damage;
	private int vulnerable;

	public SuccubusGeneAction(AbstractPlayer player, AbstractMonster monster, int damage, int vulnerable) {
		super(player, monster);
		this.source = player;
		this.target = monster;
		this.damage = damage;
		this.vulnerable = vulnerable;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DEBUFF;
	}

	public void update() {
		if (!updateTarget()) {
			this.isDone = true;
			return;
		}
		if (damage > 0) {
			AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(
					this.target, this.source, new LustPower(this.target, this.damage)));
//			AbstractDungeon.actionManager.addToTop(new DamageAction(
//					this.target, new DamageInfo(this.source, this.damage, DamageInfo.DamageType.THORNS),
//					AttackEffect.FIRE, true));
		}
		AbstractDungeon.actionManager.addToTop(
				new ApplyPowerAction(this.target, this.source, new VulnerablePower(this.target, this.vulnerable, false), this.vulnerable));

		this.isDone = true;

		this.tickDuration();
	}
}
