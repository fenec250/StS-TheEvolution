package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import evolutionmod.powers.BramblesPower;

public class ThornDamageAction extends AbstractGameAction {

	private int baseDamage;

	public ThornDamageAction(AbstractPlayer player, AbstractMonster monster, int baseDamage) {
		this.source = player;
		this.target = monster;
		this.baseDamage = baseDamage;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DAMAGE;
	}

	public void update() {
		if ((AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
			this.isDone = true;
			return;
		}
		int damage = baseDamage;
		if (this.source.hasPower(ThornsPower.POWER_ID)) {
			damage += this.source.getPower(ThornsPower.POWER_ID).amount;
		}
		if (this.source.hasPower(BramblesPower.POWER_ID)) {
			damage += this.source.getPower(BramblesPower.POWER_ID).amount;
		}
		AbstractDungeon.actionManager.addToTop(new DamageAction(
				this.target, new DamageInfo(this.source, damage,
				DamageInfo.DamageType.NORMAL), AttackEffect.NONE, true));

		this.isDone = true;

		this.tickDuration();
	}
}
