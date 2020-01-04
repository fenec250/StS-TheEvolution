package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.powers.BramblesPower;
import evolutionmod.powers.LoseThornsPower;
import evolutionmod.powers.MarkPower;

public class PlantGeneAction extends AbstractGameAction {

	private int thorn;
	private int weak;

	public PlantGeneAction(AbstractPlayer player, AbstractMonster monster, int thorn, int weak) {
		this.source = player;
		this.target = monster;
		this.thorn = thorn;
		this.weak = weak;
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
				new ApplyPowerAction(this.target, this.source, new WeakPower(this.target, this.weak, false), this.weak));
		AbstractDungeon.actionManager.addToTop(
				new ApplyPowerAction(this.source, this.source, new BramblesPower(this.source, this.thorn), this.thorn));

		this.isDone = true;

		this.tickDuration();
	}
}
