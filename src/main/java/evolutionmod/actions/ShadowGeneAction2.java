package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.powers.HeartBreakerPower;
import evolutionmod.powers.LustPower;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShadowGeneAction2 extends AbstractHalfTargetedAction {

	private int damage;
	private int weak;

	public ShadowGeneAction2(AbstractPlayer player, AbstractMonster monster, int damage, int weak) {
		super(player, monster);
		this.source = player;
		this.damage = damage;
		this.weak = weak;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = (this.target == null || this.target.isDeadOrEscaped()) ? ActionType.DEBUFF
				: monster.hasPower(WeakPower.POWER_ID) ? ActionType.DAMAGE : ActionType.DEBUFF;
	}

	public void update() {
		if (!updateTarget()) {
			this.isDone = true;
			return;
		}
		List<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters
				.stream().filter(m -> !m.isDeadOrEscaped() && m.hasPower(WeakPower.POWER_ID))
				.collect(Collectors.toList());
		for (int i = 0; i < monsters.size(); ++i) {
			AbstractMonster monster = monsters.get(i);
			int damage = this.damage;
			if (this.source.hasPower(HeartBreakerPower.POWER_ID)) {
				damage += this.source.getPower(HeartBreakerPower.POWER_ID).amount
						* monster.powers.stream().filter((p)->p.type == AbstractPower.PowerType.DEBUFF).count();
			}
			AbstractDungeon.actionManager.addToTop(new DamageAction(
					monster, new DamageInfo(this.source, damage, DamageInfo.DamageType.THORNS),
					AttackEffect.FIRE, true));
		}
		if (monsters.size() < 1) {
			AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(
					this.target, this.source, new WeakPower(this.target, this.weak, false), this.weak));
		}

		this.isDone = true;
		this.tickDuration();
	}
	public boolean updateTarget() {
		boolean s = super.updateTarget();
		this.actionType = this.target.hasPower(WeakPower.POWER_ID)?ActionType.DAMAGE:ActionType.DEBUFF;
		return s;
	}
}
