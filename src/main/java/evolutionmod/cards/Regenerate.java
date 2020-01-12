package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.BramblesPower;

public class Regenerate
        extends CustomCard {
    public static final String ID = "evolutionmod:Regenerate";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/PlantForm.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 8;
    private static final int UPGRADE_BLOCK_AMT = 3;
//    private static final int THORN_AMT = 2;
//    private static final int UPGRADE_THORN_AMT = 1;

    public Regenerate() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
//        this.magicNumber = this.baseMagicNumber = THORN_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        AbstractCard status = p.hand.getRandomCard(CardType.STATUS, true);
        if (status != null) {
            AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(status, p.hand));
        }
        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new LizardGene()));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Regenerate();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
//            this.upgradeMagicNumber(UPGRADE_THORN_AMT);
        }
    }
}