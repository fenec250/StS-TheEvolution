package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import evolutionmod.powers.MarkPower;

public class SuccubusGeneAction extends AbstractGameAction {

	private int damage;
	private int vulnerable;

	public SuccubusGeneAction(AbstractPlayer player, AbstractMonster monster, int damage, int vulnerable) {
		this.source = player;
		this.target = monster;
		this.damage = damage;
		this.vulnerable = vulnerable;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DEBUFF;
	}

	public void update() {
		if ((AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
			this.isDone = true;
			return;
		}
		if (this.target == null) {
			this.target = AbstractDungeon.getMonsters().monsters.stream()
					.filter(m -> m.hasPower(MarkPower.POWER_ID) && !m.isDeadOrEscaped())
					.findAny()
					.orElse(AbstractDungeon.getRandomMonster());
		}
		AbstractDungeon.actionManager.addToTop(
				new ApplyPowerAction(this.target, this.source, new VulnerablePower(this.target, this.vulnerable, false), this.vulnerable));
		AbstractDungeon.actionManager.addToTop(new DamageAction(
				this.target, new DamageInfo(this.source, this.damage, DamageInfo.DamageType.THORNS), AttackEffect.NONE, true));

		this.isDone = true;

		this.tickDuration();
	}
}
