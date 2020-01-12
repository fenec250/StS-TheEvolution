package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import evolutionmod.powers.MarkPower;

public class LizardGeneAction extends AbstractGameAction {

	private int poison;

	public LizardGeneAction(AbstractPlayer player, AbstractMonster monster, int poison) {
		this.source = player;
		this.target = monster;
		this.poison = poison;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DEBUFF;
	}

	public void update() {
		if ((AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
			this.isDone = true;
			return;
		}
		if (this.target == null || this.target.isDeadOrEscaped()) {
			this.target = AbstractDungeon.getMonsters().monsters.stream()
					.filter(m -> m.hasPower(MarkPower.POWER_ID) && !m.isDeadOrEscaped())
					.findAny()
					.orElse(AbstractDungeon.getRandomMonster());
		}
		AbstractDungeon.actionManager.addToTop(
				new ApplyPowerAction(this.target, this.source, new PoisonPower(this.target, this.source, this.poison), this.poison));

		this.isDone = true;

		this.tickDuration();
	}
}
