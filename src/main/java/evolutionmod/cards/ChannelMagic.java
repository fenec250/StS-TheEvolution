package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.FateAction;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.PotencyPower;
import evolutionmod.powers.RemovePotencyPower;

public class ChannelMagic
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:ChannelMagic";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LymeanSkl.png";
    private static final int COST = 1;
    private static final int FATE_AMT = 2;
    private static final int FORM_FATE_AMT = 1;
    private static final int POTENCY_AMT = 2;
    private static final int UPGRADE_POTENCY_AMT = 1;

    public ChannelMagic() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = POTENCY_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int potency = this.magicNumber;
        int fate = FATE_AMT;
        if (this.upgraded || !AbstractGene.isPlayerInThisForm(LavafolkGene.ID)) {
            addToBot(new ChannelAction(new LavafolkGene()));
        } else {
            potency += UPGRADE_POTENCY_AMT;
        }
        if (!AbstractGene.isPlayerInThisForm(LymeanGene.ID)) {
            addToBot(new ChannelAction(new LymeanGene()));
        } else {
            fate += FORM_FATE_AMT;
        }
        final int finalPotency = potency;
        if (!this.upgraded) {
            addToBot(new FateAction(fate, l -> {
                l.forEach(c -> AbstractDungeon.player.drawPile.moveToDiscardPile(c));
                if (l.stream().allMatch(c -> c.type == CardType.SKILL)) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                            p, p, new PotencyPower(p, finalPotency)));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                            p, p, new RemovePotencyPower(p, finalPotency)));
                }
            }));
        } else {
            addToBot(new FateAction(fate, l -> {
                l.forEach(c -> AbstractDungeon.player.drawPile.moveToDiscardPile(c));
                if (l.stream().anyMatch(c -> c.type == CardType.SKILL)) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                            p, p, new PotencyPower(p, finalPotency)));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                            p, p, new RemovePotencyPower(p, finalPotency)));
                }
            }));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_POTENCY_AMT);
        }
    }
}