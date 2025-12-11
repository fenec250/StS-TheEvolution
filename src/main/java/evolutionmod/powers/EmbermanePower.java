package evolutionmod.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.common.RefundAction;
import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.LavafolkGene;

import java.util.List;
import java.util.stream.Collectors;

public class EmbermanePower extends AbstractPower {
    public static final String POWER_ID = "evolutionmod:EmbermanePower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public EmbermanePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
//        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/InsectPower84.png"), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/InsectPower32.png"), 0, 0, 32, 32);
        this.loadRegion("swivel");
        this.type = PowerType.BUFF;
        this.amount = 1;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        int energy = card.costForTurn != -1 ? card.costForTurn
                : card.cost > 0 ? card.cost
                : card.cost == -1 ? card.energyOnUse
                : 0;

        if (card.type == AbstractCard.CardType.ATTACK) {
            List<AbstractOrb> lavaOrbs = AbstractDungeon.player.orbs.stream()
                    .filter(o -> o instanceof LavafolkGene)
                    .limit(energy)
                    .collect(Collectors.toList());
            lavaOrbs.forEach(o -> {
                        this.addToBot(new EvokeSpecificOrbAction(o));
                    });
            this.addToBot(new RefundAction(card, lavaOrbs.size()));
            this.addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
            this.flash();
        }
    }
}
