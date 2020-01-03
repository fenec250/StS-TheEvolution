package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.powers.LoseThornsPower;
import evolutionmod.powers.MarkPower;

public class ThornDamageAction extends AbstractGameAction {

	private int power;

	public ThornDamageAction(AbstractPlayer player, AbstractMonster monster, int power) {
		this.source = player;
		this.target = monster;
		this.power = power;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DEBUFF;
	}

	public void update() {
		if ((AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
			this.isDone = true;
			return;
		}
		if (this.source.hasPower(ThornsPower.POWER_ID)) {
			AbstractDungeon.actionManager.addToTop(new DamageAction(
					this.target, new DamageInfo(this.source, this.source.getPower(ThornsPower.POWER_ID).amount * power,
					DamageInfo.DamageType.NORMAL), AttackEffect.NONE, true));
		}

		this.isDone = true;

		this.tickDuration();
	}
}
