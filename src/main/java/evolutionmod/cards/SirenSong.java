package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;

public class SirenSong
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:SirenSong";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/MerfolkSkl.png";
    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    //    private static final int VULNERABLE_AMT = 2;
    //    private static final int UPGRADE_VULNERABLE_AMT = 1;
    private static final int BLOCK_AMT = 4;

    public SirenSong() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
//        this.magicNumber = this.baseMagicNumber = VULNERABLE_AMT;
        this.block = this.baseBlock = BLOCK_AMT;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.getMonsters().monsters.stream()
                .filter(mo -> !mo.isDeadOrEscaped() && mo.getIntentBaseDmg() >= 0)
                .forEach(mo -> addToBot(new GainBlockAction(p, this.block)));
        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new MerfolkGene()));
//        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new SuccubusGene()));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SirenSong();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADED_COST);
//            this.upgradeMagicNumber(UPGRADE_VULNERABLE_AMT);
        }
    }
}